package br.unipar.projetointegrador.frotisapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FrotisApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrotisApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String senhaPura = "123456";
            String hashGerado = encoder.encode(senhaPura);

            System.out.println("\n\n=================================================");
            System.out.println(">>> GERADOR DE SENHA PARA O PROJETO FORTIS <<<");
            System.out.println("Senha: " + senhaPura);
            System.out.println("HASH VÁLIDO (Copie tudo abaixo):");
            System.out.println(hashGerado);
            System.out.println("=================================================\n\n");
        };
    }

    @Bean // Informa ao Spring para gerenciar este objeto
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
