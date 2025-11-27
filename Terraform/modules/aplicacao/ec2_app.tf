resource "aws_instance" "ec2_app" {
  ami                    = "ami-0360c520857e3138f"
  instance_type          = var.instance_type
  subnet_id              = var.subnet_id
  vpc_security_group_ids = [aws_security_group.app.id]
  key_name               = var.key_name

  user_data = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo yum install -y java-17-amazon-corretto
              
              # Cria diretório da aplicação
              mkdir -p /opt/contractimovel
              cd /opt/contractimovel
              
              # Baixa o .jar da aplicação (exemplo via S3)
              aws s3 cp s3://${var.s3_bucket}/contractimovel.jar /opt/contractimovel/
              
              # Executa o .jar
              nohup java -jar /opt/contractimovel/contractimovel.jar > /var/log/contractimovel.log 2>&1 &
              EOF

  tags = {
    Name = "ec2-aplicativo"
  }
}
