variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "vpc_id" {
  type = string
}

variable "subnet_id" {
  type = string
}

variable "key_name" {
  description = "Nome do par de chaves SSH configurado na AWS"
  type        = string
}

variable "s3_bucket" {
  description = "Bucket S3 onde o .jar da aplicação está armazenado"
  type        = string
}
