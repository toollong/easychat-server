package com.easychat.auth.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Author: long
 * @Date: 2022-04-30 12:53
 */
@Configuration
public class SecurityConfig {

    //    @Bean
    //    public PasswordEncoder passwordEncoder() {
    //        return new BCryptPasswordEncoder();
    //    }

    //    @Bean
    //    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //        http
    //                .cors(Customizer.withDefaults())
    //                .csrf(AbstractHttpConfigurer::disable)
    //                .sessionManagement(session -> session
    //                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //                .authorizeHttpRequests(authorize -> authorize
    //                        .antMatchers("/login*", "/logout*").permitAll()
    //                        .anyRequest().authenticated())
    //                .formLogin(login -> login
    //                        .loginProcessingUrl("/login"))
    //                .logout(logout -> logout
    //                        .logoutUrl("/logout"))
    //                .httpBasic(Customizer.withDefaults());
    //        return http.build();
    //    }
}
