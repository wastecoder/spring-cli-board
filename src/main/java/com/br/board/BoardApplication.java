package com.br.board;

import com.br.board.persistence.migration.MigrationStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.SQLException;

import static com.br.board.persistence.config.ConnectionConfig.getConnection;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		// Inicia o Spring Boot
		SpringApplication.run(BoardApplication.class, args);

		// Executa a migração após o contexto estar pronto
		try (Connection connection = getConnection()) {
			new MigrationStrategy(connection).executeMigration();
		} catch (SQLException e) {
			e.printStackTrace(); // Lide melhor com a exceção em produção
		}
	}
}