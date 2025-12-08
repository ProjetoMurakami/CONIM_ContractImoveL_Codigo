# Projeto: CONIM — Infraestrutura distribuída AWS + Ubuntu + Build de Imagem Linux

## Índice:

### Visão geral e objetivo;
### Diagrama de Arquitetura AWS;
### Pré-requisitos;
### Estrutura de rede (VPC, subnets, route tables);
### Security Groups e regras;
### Componentes AWS (ECR, ECS/Fargate, ALB, RDS, IAM)
### Passo a passo — criação manual (com comandos)
### Docker: Dockerfile, build e push para ECR
### ECS Fargate: Task Definition e Service.
### RDS: criação e ajustes (MySQL)
### Packer: build de AMI (imagem Linux)

Anexos: snippets de config (persistence.xml, setenv.sh, systemd)

## Visão geral e objetivo

Orientações para criação do ambinete de produção distribuída do ConimContractImovel realizado em Java/Tomcat (WAR) empacotada em Docker e executada em ECS Fargate, com imagens armazenadas no ECR, balanceamento via ALB, banco relacional em RDS (MySQL).

## Diagrama de arquitetura (resumo)

![Diagrama de Arquiteura](/imagensAWS/CONIM%20-%20Diagrama%20AWS.png)
VPC (us-east-1)
2 AZs (us-east-1a, us-east-1b)
Em cada AZ:
1 Public Subnet (hosts/ALB)
1 Private Subnet (ECS tasks + RDS)

Internet Gateway para tráfego público
Route Table pública associada às public subnets
ALB em public subnets (Listener 80) → Target group apontando para ECS tasks (Fargate)
ECR para armazenar imagens
ECS Cluster com serviços Fargate (tasks que executam o container)
RDS (MySQL) em subnets privadas (Multi-AZ)

Security Groups:
ALB SG: libera 0.0.0.0/0 porta 80
ECS task SG: permite tráfego do ALB SG na porta 8080 
RDS SG: permite tráfego apenas do ECS SG ou bastion SG na porta 3306

## Pré-requisitos

### Local:
1. Git
2. Docker (para build local)
3. AWS CLI v2 (configurado com credenciais)
4. Terraform (se for IaC)

### Conta AWS:
Permissões para criar VPC, subnets, NAT gateway, ECR, ECS, ALB, RDS, IAM, CloudWatch

## Estrutura de rede — recomendação (CIDRs)

1. VPC: 10.0.0.0/16
2. Public Subnet A (us-east-1a): 10.0.1.0/24
3. Private Subnet A (us-east-1a): 10.0.2.0/24
4. Public Subnet B (us-east-1b): 10.0.3.0/24
5. Private Subnet B (us-east-1b): 10.0.4.0/24

## Security Groups

### ALB-SG

1. Inbound: 0.0.0.0/0 TCP 80 (HTTP)
2. Inbound: 0.0.0.0/0 TCP 443 (HTTPS) — optional
3 .Outbound: All

### ECS-Task-SG
1. Inbound: ALB-SG (source security group) TCP 8080 (container port)
2. Outbound: 0.0.0.0/0 or restricted to RDS-SG 3306 for DB calls

### RDS-SG
1. Inbound: ECS-Task-SG TCP 3306
2 .Inbound: Bastion SG SSH (if needed)
3. Outbound: default

## Componentes AWS — Funcionamento e uso
1. ECR: repositório privado para armazenar docker images.
2. ECS + Fargate: execução sem gerenciar instâncias 
3. ALB: distribui para target group
4. RDS: provisionar MySQL (port 3306) em subnets privadas; 

## Passo a passo — criação manual (com comandos)

### Criar VPC e subnets 


#### criar VPC
aws ec2 create-vpc \
    --cidr-block 10.0.0.0/16 \
    --amazon-provided-ipv6-cidr-block \
    --tag-specifications 'ResourceType=vpc,Tags=[{Key=Name,Value=Conim-vpc}]'
Acesse a aba VPC na AWS e anote o ID vinculado, será necessário para dar outros comandos. Será referenciado como VPC-ID em outros pontos.

#### criar subnets

us-east-1a
~~~
aws ec2 create-subnet \
    --vpc-id [VPC-ID] \
    --cidr-block 10.0.0.0/24 \
    --availability-zone us-east-1a \
    --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=Conim-subnet-public1-us-east-1a}]'
~~~

us-east-1b
~~~
aws ec2 create-subnet \
    --vpc-id [VPC-ID] \
    --cidr-block 10.0.16.0/24 \
    --availability-zone us-east-1b \
    --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=Conim-subnet-public2-us-east-1b}]'
~~~

us-east-1a
~~~
aws ec2 create-subnet \
    --vpc-id [VPC-ID] \
    --cidr-block 10.0.128.0/24 \
    --availability-zone us-east-1a \
    --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=Conim-subnet-private1-us-east-1a}]'
~~~

us-east-1b
~~~
aws ec2 create-subnet \
    --vpc-id [VPC-ID] \
    --cidr-block 10.0.176.0/24 \
    --availability-zone us-east-1b \
    --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=Conim-subnet-private4-us-east-1b}]'
~~~

#### criar IGW e associar VPC
Anotar id-gerado: igwID
~~~
aws ec2 create-internet-gateway \
    --tag-specifications 'ResourceType=internet-gateway,Tags=[{Key=Name,Value=Conim-igw}]'

aws ec2 attach-internet-gateway \
    --vpc-id [VPC-ID] \
    --internet-gateway-id [igwID]
~~~

#### criar route table pública e associar
Anotar id-gerado: routeTableID
~~~
aws ec2 create-route-table \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=route-table,Tags=[{Key=Name,Value=Conim-rtb-public}]'
~~~

Adicionar rota padrão IPv4 para IGW
~~~
aws ec2 create-route \
    --route-table-id [routeTableID] \
    --destination-cidr-block 0.0.0.0/0 \
    --gateway-id [igwID]
~~~
Adicionar rota padrão IPv6 para IGW
~~~
aws ec2 create-route \
    --route-table-id [routeTableID] \
    --destination-ipv6-cidr-block ::/0 \
    --gateway-id [igwID]
~~~
#### criar route table privada
~~~
aws ec2 create-route-table \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=route-table,Tags=[{Key=Name,Value=Conim-rtb-private1-us-east-1a}]'
~~~
~~~
aws ec2 create-route-table \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=route-table,Tags=[{Key=Name,Value=Conim-rtb-private2-us-east-1a}]'
~~~
#### Criar Security Groups (exemplo)
~~~
aws ec2 create-security-group \
    --group-name "ALB-SG" \
    --description "SG para Application Load Balancer" \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=Conim-alb-sg}]'
~~~
### ALB SG
Anotar id-gerado: sg-alb-id
~~~
aws ec2 create-security-group \
    --group-name "ALB-SG" \
    --description "SG para Application Load Balancer" \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=Conim-alb-sg}]'
~~~
Permitir HTTP/HTTPS
~~~
aws ec2 authorize-security-group-ingress \
    --group-id [sg-alb-id] \
    --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress \
    --group-id [sg-alb-id] \
    --protocol tcp --port 443 --cidr 0.0.0.0/0
~~~

### ECS Tasks SG
Anotar id-gerado: sg-ecs-id
~~~
aws ec2 create-security-group \
    --group-name "ECS-Tasks-SG" \
    --description "SG para tarefas ECS" \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=Conim-ecs-tasks-sg}]'
~~~
### permitir ALB -> ECS 8080
~~~
Permitir tráfego do ALB na porta 8080
aws ec2 authorize-security-group-ingress \
    --group-id [sg-ecs-id] \
    --protocol tcp --port 8080 \
    --source-group [sg-alb-id]
~~~

### RDS SG 
~~~
aws ec2 create-security-group \
    --group-name "RDS-SG" \
    --description "SG para RDS MySQL" \
    --vpc-id [VPC-ID] \
    --tag-specifications 'ResourceType=security-group,Tags=[{Key=Name,Value=Conim-rds-sg}]'
~~~
Permitir apenas do SG do ECS na porta 3306
~~~
aws ec2 authorize-security-group-ingress \
    --group-id [sg-rds-id] \
    --protocol tcp --port 3306 \
    --source-group [sg-ecs-id]
~~~

## VPC Endpoint
~~~
aws ec2 create-vpc-endpoint \
    --vpc-id [VPC-ID] \
    --service-name com.amazonaws.us-east-1.s3 \
    --vpc-endpoint-type Gateway \
    --route-table-ids [Conim-rtb-public-id] [Conim-rtb-private1-us-east-1a-id] [Conim-rtb-private2-us-east-1a-id] \
    --tag-specifications 'ResourceType=vpc-endpoint,Tags=[{Key=Name,Value=Conim-vpce-s3}]'
~~~

## Criar subnet-group
~~~
aws rds create-db-subnet-group \
    --db-subnet-group-name conim-db-subnet-group \
    --db-subnet-group-description "Subnet group for RDS MySQL in Conim VPC" \
    --subnet-ids [Conim-rtb-public-id] [Conim-rtb-private1-us-east-1a-id] [Conim-rtb-private2-us-east-1a-id]
~~~
## Criar RDS MySQL 
~~~
aws rds create-db-instance \
    --db-instance-identifier conim-mysql \
    --db-instance-class db.t3.micro \
    --engine mysql \
    --allocated-storage 20 \
    --master-username admin \
    --master-user-password "SENHA_SEGURA" \
    --vpc-security-group-ids [sg-rds-id] \
    --db-subnet-group-name [default-vpc-subnet-group] \
    --backup-retention-period 7 \
    --multi-az true \
    --storage-type gp2 \
    --engine-version 8.0 \
    --tags Key=Name,Value=Conim-MySQL 
    --no-publicly-accessible
    
~~~

## Criar ECR repo
~~~
aws ecr create-repository \
    --repository-name conim-app \
    --image-tag-mutability MUTABLE \
    --image-scanning-configuration scanOnPush=true
~~~

## Criar ECS Cluster
~~~
aws ecs create-cluster \
  --cluster-name conim-cluster \
  --capacity-providers FARGATE FARGATE_SPOT \
  --default-capacity-provider-strategy \
      capacityProvider=FARGATE,weight=1,base=1 \
      capacityProvider=FARGATE_SPOT,weight=3 \
  --settings name=containerInsights,value=enabled \
  --tags key=Name,value=Conim-ECS-Cluster \
         key=Environment,value=Production \
         key=Project,value=Conim
~~~
## Task Execution Role
### Criar policy de confiança para a role
~~~
cat > ecs-task-execution-trust.json << EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
~~~
### Criar a role
~~~
aws iam create-role \
    --role-name ecsTaskExecutionRole \
    --assume-role-policy-document file://ecs-task-execution-trust.json \
    --description "ECS Task Execution Role for Conim App"
~~~
### Anexar política gerenciada do ECS
~~~
aws iam attach-role-policy \
    --role-name ecsTaskExecutionRole \
    --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
~~~
### Criar política personalizada para acesso ao ECR
~~~
cat > ecr-access-policy.json << EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "secretsmanager:GetSecretValue",
        "ssm:GetParameters"
      ],
      "Resource": "*"
    }
  ]
}
EOF
~~~
~~~
aws iam create-policy \
    --policy-name ConimECSECRAccess \
    --policy-document file://ecr-access-policy.json
~~~
~~~
aws iam attach-role-policy \
    --role-name ecsTaskExecutionRole \
    --policy-arn arn:aws:iam::070988404371:policy/ConimECSECRAccess
~~~

## Criar ECS Task Definition
### Criar arquivo de definição da task
~~~
cat > conim-task-definition.json << EOF
{
  "family": "conim-app-task",
  "networkMode": "awsvpc",
  "executionRoleArn": "arn:aws:iam::070988404371:role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::070988404371:role/ecsTaskExecutionRole",
  "cpu": "512",
  "memory": "1024",
  "requiresCompatibilities": ["FARGATE"],
  "runtimePlatform": {
    "cpuArchitecture": "X86_64",
    "operatingSystemFamily": "LINUX"
  },
  "containerDefinitions": [
    {
      "name": "conim-app-container",
      "image": "070988404371.dkr.ecr.us-east-1.amazonaws.com/conim-app:latest",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "hostPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "ENVIRONMENT",
          "value": "production"
        },
        {
          "name": "DB_HOST",
          "value": "conim-mysql.cg6c8c8c8c8c.us-east-1.rds.amazonaws.com"
        },
        {
          "name": "DB_NAME",
          "value": "conimdb"
        }
      ],
      "secrets": [
        {
          "name": "DB_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:070988404371:secret:conim-db-password"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/conim-app",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": ["CMD-SHELL", "curl -f http://localhost:8080/health || exit 1"],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ],
  "tags": [
    {
      "key": "Name",
      "value": "Conim-App-Task"
    },
    {
      "key": "Environment",
      "value": "Production"
    }
  ]
}
EOF
~~~
### Registrar a task definition
~~~
aws ecs register-task-definition \
    --cli-input-json file://conim-task-definition.json
~~~
### Log Group CloudWatch
~~~
aws logs create-log-group \
    --log-group-name "/ecs/conim-app" \
    --tags "Environment=Production,Project=Conim"
~~~
## Criar Target Group para o ALB
### Criar target group para o ALB (porta 8080 - aplicação)
~~~
aws elbv2 create-target-group \
    --name conim-app-tg \
    --protocol HTTP \
    --port 8080 \
    --vpc-id vpc-0c9f1f3622ad1b4ee \
    --target-type ip \
    --health-check-protocol HTTP \
    --health-check-path "/health" \
    --health-check-interval-seconds 30 \
    --health-check-timeout-seconds 5 \
    --healthy-threshold-count 2 \
    --unhealthy-threshold-count 2 \
    --matcher "HttpCode=200-399" \
    --ip-address-type ipv4 \
    --tags "Key=Name,Value=Conim-App-TG Key=Environment,Value=Production"
~~~
### Criar Application Load Balancer
~~~
aws elbv2 create-load-balancer \
    --name conim-alb \
    --subnets [Conim-subnet-public1-us-east-1a] , [Conim-subnet-public1-us-east-1b] \
    --security-groups [sg-alb-id] \
    --scheme internet-facing \
    --type application \
    --ip-address-type ipv4 \
    --tags "Key=Name,Value=Conim-ALB Key=Environment,Value=Production"
~~~
### Criar listener no ALB
~~~
ALB_ARN=$(aws elbv2 create-load-balancer ... --query 'LoadBalancers[0].LoadBalancerArn' --output text)
TG_ARN=$(aws elbv2 create-target-group ... --query 'TargetGroups[0].TargetGroupArn' --output text)

aws elbv2 create-listener \
    --load-balancer-arn $ALB_ARN \
    --default-actions Type=forward,TargetGroupArn=$TG_ARN
~~~
### Criar ECS Service
~~~
aws ecs create-service \
    --cluster conim-cluster \
    --service-name conim-service \
    --task-definition conim-app-task:1 \
    --desired-count 2 \
    --launch-type FARGATE \
    --platform-version "LATEST" \
    --scheduling-strategy REPLICA \
    --deployment-controller type=ECS \
    --enable-execute-command \
    --network-configuration "awsvpcConfiguration={subnets=[$SUBNET_PRIV_1A,$SUBNET_PRIV_1B],securityGroups=[$SG_ECS_ID],assignPublicIp=DISABLED}" \
    --load-balancers "targetGroupArn=arn:aws:elasticloadbalancing:us-east-1:070988404371:targetgroup/conim-app-tg/abcdef1234567890,containerName=conim-app-container,containerPort=8080" \
    --health-check-grace-period-seconds 60 \
    --deployment-configuration "maximumPercent=200,minimumHealthyPercent=50" \
    --tags "key=Name,value=Conim-App-Service key=Environment,value=Production"
~~~
### Build da imagem local e push para ECR 
Acesse o terminal e a pasta ContractImovel
- Execute mvn clean install
- Rode  docker build -t contract-imovel-app:latest .
- docker tag contract-imovel-app:latest 070988404371.dkr.ecr.us-east-1.amazonaws.com/conim-app:latest
Verifique pelo Docker Dessktop ou via terminal para verificar a criação da imagem
~~~
aws ecr get-login-password --region "${REGION}" | \
    docker login \
        --username AWS \
        --password-stdin \
        "${ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com"
~~~
Determine o tipo de tag:
~~~
- docker push "${VERSIONED_TAG_FULL}"
- docker push "${LATEST_TAG}"
~~~
~~~
TASK_DEF_FILE="conim-task-definition.json"
BACKUP_FILE="conim-task-definition.backup.${TIMESTAMP}.json"
~~~
### Criar task definition
~~~
if [ -f "$TASK_DEF_FILE" ]; then
    # Fazer backup da task definition atual
    cp "$TASK_DEF_FILE" "$BACKUP_FILE"
    
    # Método 1: Usando sed (se a estrutura do JSON for simples)
    sed -i.bak "s|\"image\": \".*\"|\"image\": \"${VERSIONED_TAG_FULL}\"|" "$TASK_DEF_FILE"
    
    # Método alternativo: Usando jq (recomendado se tiver jq instalado)
    if command -v jq &> /dev/null; then
        jq --arg IMAGE "$VERSIONED_TAG_FULL" \
           '.containerDefinitions[0].image = $IMAGE' \
           "$BACKUP_FILE" > "$TASK_DEF_FILE"
    fi
    TASK_DEF_ARN=$(aws ecs register-task-definition \
        --cli-input-json "file://${TASK_DEF_FILE}" \
        --region "${REGION}" \
        --query 'taskDefinition.taskDefinitionArn' \
        --output text)
    TASK_REVISION=$(aws ecs describe-task-definition \
        --task-definition "${TASK_FAMILY}" \
        --region "${REGION}" \
        --query 'taskDefinition.revision' \
        --output text)
else
    TASK_REVISION=$(aws ecs describe-task-definition \
        --task-definition "${TASK_FAMILY}" \
        --region "${REGION}" \
        --query 'taskDefinition.revision' \
        --output text)
fi
~~~
~~~
aws ecs update-service \
    --cluster "${CLUSTER_NAME}" \
    --service "${SERVICE_NAME}" \
    --task-definition "${TASK_FAMILY}:${TASK_REVISION}" \
    --region "${REGION}" \
    --force-new-deployment
~~~
~~~
aws ecs wait services-stable \
    --cluster "${CLUSTER_NAME}" \
    --services "${SERVICE_NAME}" \
    --region "${REGION}"
~~~
~~~
aws ecs describe-services \
    --cluster "${CLUSTER_NAME}" \
    --services "${SERVICE_NAME}" \
    --region "${REGION}" \
    --query 'services[0].[serviceName,status,desiredCount,runningCount]' \
    --output table
~~~
~~~
aws ecs list-tasks \
    --cluster "${CLUSTER_NAME}" \
    --service-name "${SERVICE_NAME}" \
    --region "${REGION}" \
    --query 'taskArns' \
    --output table
~~~