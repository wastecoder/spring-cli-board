package com.br.board;

import com.br.board.persistence.migration.MigrationStrategy;
import com.br.board.ui.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.SQLException;

import static com.br.board.persistence.config.ConnectionConfig.getConnection;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

	@Bean
	CommandLineRunner run() {
		return args -> {
			try (Connection connection = getConnection()) {
				new MigrationStrategy(connection).executeMigration();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Chamar o menu após a migração
			new MainMenu().execute();
		};
	}
}
