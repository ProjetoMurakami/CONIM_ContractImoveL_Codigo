output "vpc_id" {
  value       = aws_vpc.main.id
}

output "public_subnets" {
  value       = [aws_subnet.public1.id, aws_subnet.public2.id]
}

output "ecs_cluster_name" {
  value       = aws_ecs_cluster.my_cluster.name
}

output "ecs_service_name" {
  value       = aws_ecs_service.my_service.name
}

output "task_definition_arn" {
  value       = aws_ecs_task_definition.my_task.arn
}
