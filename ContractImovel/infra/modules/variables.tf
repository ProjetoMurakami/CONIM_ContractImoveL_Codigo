  variable "aws_region" {
    type        = string
    default     = "us-east-1"
  }

  variable "vpc_cidr" {
    type        = string
    default     = "10.0.0.0/16"
  }

  variable "public_subnet_cidr_a" {
    type        = string
    default     = "10.0.1.0/24"
  }

  variable "public_subnet_cidr_b" {
    type        = string
    default     = "10.0.2.0/24"
  }

  variable "private_subnet_cidr_a" {
    type        = string
    default     = "10.0.3.0/24"
  }

  variable "private_subnet_cidr_b" {
    type        = string
    default     = "10.0.4.0/24"
  }

  variable "availability_zone_a" {
    type        = string
    default     = "us-east-1a"
  }

  variable "availability_zone_b" {
    type        = string
    default     = "us-east-1b"
  }

  variable "ecr_repository_name" {
    type        = string
    default     = "contractimovel-repo"
  }

  variable "ecs_cluster_name" {
    type        = string
    default     = "my-ecs-cluster"
  }

  variable "ecs_service_name" {
    type        = string
    default     = "my-ecs-service"
  }

  variable "ecs_task_family" {
    type        = string
    default     = "my-task-family"
  }

  variable "container_name" {
    type        = string
    default     = "my-container"
  }

  variable "lab_role_name" {
    type        = string
    default     = "LabRole"
  }

