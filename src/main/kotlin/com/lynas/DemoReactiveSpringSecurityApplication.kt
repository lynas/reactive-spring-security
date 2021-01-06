package com.lynas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@SpringBootApplication
class DemoReactiveSpringSecurityApplication

fun main(args: Array<String>) {
    runApplication<DemoReactiveSpringSecurityApplication>(*args)
}


@Component
class AppReactiveUserDetailService(val appUserRepository: AppUserRepository) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return appUserRepository.findAppUserByEmail(username)
            .map { CustomUser(it) }
    }

    class CustomUser(appUser: AppUser) :
        User(appUser.email, appUser.password, AuthorityUtils.createAuthorityList(appUser.roles)), UserDetails {
        override fun isEnabled() = true

        override fun isAccountNonExpired() = true

        override fun isAccountNonLocked() = true

        override fun isCredentialsNonExpired() = true
    }
}

@Table
open class AppUser(
    @Id
    val id: Long,
//    val firstName: String,
//    val lastName: String,
    val email: String,
    val password: String,
    val roles: String
)

interface AppUserRepository : ReactiveCrudRepository<AppUser,Long>{

    fun findAppUserByEmail(email: String) : Mono<AppUser>
}

@EnableWebFluxSecurity
class AppSecurityConfig{

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        http
            .authorizeExchange()
            .pathMatchers("/public").permitAll()
            .anyExchange().authenticated()
            .and()
            .httpBasic().and()
            .formLogin()
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}

@RestController
class DemoController{

    @GetMapping("/private")
    suspend fun private() = "Private content"

    @GetMapping("/public")
    suspend fun public() = "Public content"


}















