resource "aws_security_group" "bd" {
  name   = "bd-security-group"
  vpc_id = var.vpc_id

  ingress {
    description = "Permitir conexão da aplicação"
    from_port   = 12555
    to_port     = 12555
    protocol    = "tcp"
    security_groups = [aws_security_group.app.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "bd-security-group"
  }
}

resource "aws_instance" "ec2_bd" {
  ami                    = "ami-0360c520857e3138f"
  instance_type          = var.instance_type
  subnet_id              = var.subnet_id
  vpc_security_group_ids = [aws_security_group.bd.id]

  tags = {
    Name = "ec2-banco-de-dados"
  }
}
