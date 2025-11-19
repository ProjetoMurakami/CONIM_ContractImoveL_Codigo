resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "vpc-principal"
  }
}

resource "aws_subnet" "app" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    Name = "subnet-app"
  }
}

resource "aws_subnet" "bd" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "subnet-bd"
  }
}

output "vpc_id" {
  value = aws_vpc.main.id
}

output "subnet_app_id" {
  value = aws_subnet.app.id
}

output "subnet_bd_id" {
  value = aws_subnet.bd.id
}
