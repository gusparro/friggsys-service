# ========================================
# Makefile - SAPED Service
# ========================================

# Variables
IMAGE_NAME := friggsys-service
CONTAINER_NAME := friggsys-service
PORT := 8080

# Database variables
DB_URL := jdbc:postgresql://host.docker.internal:5432/friggsys
DB_USERNAME := postgres
DB_PASSWORD := postgres

# Colors
GREEN := \033[0;32m
BLUE := \033[0;34m
YELLOW := \033[1;33m
NC := \033[0m

.PHONY: help deploy build run stop restart clean logs logs-clear

# ========================================
# Help
# ========================================
help:
	@echo "$(BLUE)╔════════════════════════════════════════════════════════════════╗$(NC)"
	@echo "$(BLUE)║$(NC)               $(GREEN)FriggSys Service - Available Commands$(NC)            $(BLUE)║$(NC)"
	@echo "$(BLUE)╠════════════════════════════════════════════════════════════════╣$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make deploy$(NC)      - Full build and deploy (Maven + Docker)  	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make build$(NC)       - Build only (Maven + Docker)             	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make run$(NC)         - Run container only                      	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make stop$(NC)        - Stop container                         	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make restart$(NC)     - Restart container                       	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make clean$(NC)       - Remove container and image              	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make logs$(NC)        - Show application log file               	 $(BLUE)║$(NC)"
	@echo "$(BLUE)║$(NC)  $(GREEN)make logs-clear$(NC)  - Clear application log files                $(BLUE)║$(NC)"
	@echo "$(BLUE)╚════════════════════════════════════════════════════════════════╝$(NC)"

# ========================================
# Full deploy
# ========================================
deploy: clean maven-build docker-build docker-run
	@echo "$(GREEN)✓ Deploy completed successfully!$(NC)"

# ========================================
# Maven build
# ========================================
maven-build:
	@echo "$(BLUE)==> Building with Maven...$(NC)"
	@mvn clean package -DskipTests
	@echo "$(GREEN)✓ Maven build completed!$(NC)"

# ========================================
# Docker build
# ========================================
docker-build:
	@echo "$(BLUE)==> Building Docker image...$(NC)"
	@docker build -t $(IMAGE_NAME) .
	@echo "$(GREEN)✓ Image created!$(NC)"

# ========================================
# Full build (Maven + Docker)
# ========================================
build: maven-build docker-build
	@echo "$(GREEN)✓ Build completed!$(NC)"

# ========================================
# Run container
# ========================================
docker-run:
	@echo "$(BLUE)==> Starting container...$(NC)"
	@docker run -d \
		--name $(CONTAINER_NAME) \
		-p $(PORT):$(PORT) \
		-e DATABASE_URL="$(DB_URL)" \
		-e DATABASE_USERNAME="$(DB_USERNAME)" \
		-e DATABASE_PASSWORD="$(DB_PASSWORD)" \
		$(IMAGE_NAME)
	@echo "$(GREEN)✓ Container started!$(NC)"

# ========================================
# Run (without rebuild)
# ========================================
run: docker-run

# ========================================
# Stop container
# ========================================
stop:
	@echo "$(YELLOW)Stopping container...$(NC)"
	@docker stop $(CONTAINER_NAME) 2>/dev/null || true
	@echo "$(GREEN)✓ Container stopped!$(NC)"

# ========================================
# Restart
# ========================================
restart: stop run

# ========================================
# Logs
# ========================================
logs:
	@echo "$(BLUE)==> Showing application log file...$(NC)"
	@docker exec -it $(CONTAINER_NAME) sh -c 'cd logs 2>/dev/null && tail -f *.log 2>/dev/null || tail -f /secad-sphere/logs/*.log 2>/dev/null || echo "$(RED)No log files found$(NC)"'

logs-clear:
	@echo "$(YELLOW)==> Clearing application log files...$(NC)"
	@docker exec $(CONTAINER_NAME) sh -c 'rm -f logs/*.log 2>/dev/null || rm -f /secad-sphere/logs/*.log 2>/dev/null' && \
	echo "$(GREEN)✓ Log files cleared!$(NC)" || \
	echo "$(RED)✗ Failed to clear logs (container might not be running)$(NC)"

# ========================================
# Clean
# ========================================
clean:
	@echo "$(YELLOW)Cleaning up resources...$(NC)"
	@docker stop $(CONTAINER_NAME) 2>/dev/null || true
	@docker rm $(CONTAINER_NAME) 2>/dev/null || true
	@echo "$(GREEN)✓ Resources cleaned!$(NC)"
	@docker rmi $(IMAGE_NAME) 2>/dev/null || true
	@echo "$(GREEN)✓ Image destroyed!$(NC)"