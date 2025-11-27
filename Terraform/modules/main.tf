module "conectividade" {
  source = "./conectividade"
}

module "aplicacao" {
  source = "./aplicacao"

  # Variáveis obrigatórias do módulo de aplicação
  vpc_id        = module.conectividade.vpc_id
  subnet_id     = module.conectividade.subnet_app_id
  instance_type = "t3.micro"

  # Novos parâmetros para o deploy
  key_name   = "minha-chave-ssh"         # Substitua pelo nome da sua key na AWS
  s3_bucket  = "meu-bucket-deploy"       # Substitua pelo nome do seu bucket S3
}
