package com.example.cheffin.config;

import com.example.cheffin.model.Post;
import com.example.cheffin.model.User;
import com.example.cheffin.repository.PostRepository;
import com.example.cheffin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Skip if data already exists
            if (userRepository.count() > 0) {
                return;
            }

            // Create regular user
            User user = User.builder()
                    .username("user")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("password"))
                    .role(User.Role.USER)
                    .profilePicture("https://ui-avatars.com/api/?name=User&background=random")
                    .build();
            userRepository.save(user);

            // Create chef users
            User chef1 = User.builder()
                    .username("chef_mario")
                    .email("mario@example.com")
                    .password(passwordEncoder.encode("password"))
                    .role(User.Role.CHEF)
                    .profilePicture("https://ui-avatars.com/api/?name=Mario&background=random")
                    .specialty("Italian Cuisine")
                    .bio("Passionate Italian chef with 15 years of experience in authentic pasta and pizza.")
                    .experience(15)
                    .featured(true)
                    .build();
            userRepository.save(chef1);

            User chef2 = User.builder()
                    .username("chef_sarah")
                    .email("sarah@example.com")
                    .password(passwordEncoder.encode("password"))
                    .role(User.Role.CHEF)
                    .profilePicture("https://ui-avatars.com/api/?name=Sarah&background=random")
                    .specialty("Pastry & Desserts")
                    .bio("Award-winning pastry chef specializing in French desserts and artisanal bread.")
                    .experience(10)
                    .featured(true)
                    .build();
            userRepository.save(chef2);

            User chef3 = User.builder()
                    .username("chef_takashi")
                    .email("takashi@example.com")
                    .password(passwordEncoder.encode("password"))
                    .role(User.Role.CHEF)
                    .profilePicture("https://ui-avatars.com/api/?name=Takashi&background=random")
                    .specialty("Japanese Cuisine")
                    .bio("Trained in Tokyo, specializing in sushi, ramen, and traditional Japanese dishes.")
                    .experience(12)
                    .featured(true)
                    .build();
            userRepository.save(chef3);

            // Create posts
            Post post1 = Post.builder()
                    .user(chef1)
                    .title("Homemade Pasta Carbonara")
                    .description("My authentic recipe for the perfect carbonara. Made with fresh eggs, pecorino romano, and guanciale.")
                    .contentImages(List.of("https://images.unsplash.com/photo-1612874742237-6526221588e3?ixlib=rb-4.0.3"))
                    .build();
            postRepository.save(post1);

            Post post2 = Post.builder()
                    .user(chef2)
                    .title("Classic French Macarons")
                    .description("Perfect your macaron technique with this foolproof recipe. Crisp shells with smooth ganache filling.")
                    .contentImages(List.of("https://images.unsplash.com/photo-1569864358642-9d1684040f43?ixlib=rb-4.0.3"))
                    .build();
            postRepository.save(post2);

            Post post3 = Post.builder()
                    .user(chef3)
                    .title("Tonkotsu Ramen from Scratch")
                    .description("Learn how to make rich, creamy tonkotsu broth and handmade ramen noodles for the ultimate bowl of comfort.")
                    .contentImages(List.of("https://images.unsplash.com/photo-1614563637806-1d0e645e0940?ixlib=rb-4.0.3"))
                    .build();
            postRepository.save(post3);
        };
    }
}
