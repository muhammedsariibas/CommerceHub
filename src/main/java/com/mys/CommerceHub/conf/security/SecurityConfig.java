package com.mys.CommerceHub.conf.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final CommerceHubUserDetailService commerceHubUserDetailService;
    private final JwtTokenFilter jwtTokenFilter;
    private final HttpAccessDeniedHandler accessDeniedHandler;
    private final HttpAuthEntryPoint entryPoint;

    @Value("${app.security.conf.crossOrigins}")
    private String crossOrigins;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {



        /*http.authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();*/
        // Enable CORS and disable CSRF


        http = http
                .cors()

                .and()

                .csrf()
                .disable()

                .authorizeHttpRequests()

                .requestMatchers("/public/**")
                .permitAll()

                .requestMatchers("/api/**")
                .authenticated()

                .and()


                .authenticationProvider(authenticationProvider)
                .userDetailsService(commerceHubUserDetailService)

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()

                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(entryPoint)

                .and()

                .addFilterBefore(
                        jwtTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        System.out.println("Cross origin : " + crossOrigins);

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(crossOrigins));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
