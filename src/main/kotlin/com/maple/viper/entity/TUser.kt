package com.maple.viper.entity

import com.maple.viper.dto.request.UserRegistRequest
import com.maple.viper.form.UserRegistForm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_user")
data class TUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "account_id", nullable = false)
    val accountId: Int,
    @Column(name = "email", nullable = false, unique = true)
    val email: String,
    @Column(name = "password", nullable = false)
    val pass: String,
    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,
) : UserDetails {
    companion object {
        fun generateInsertModel(form: UserRegistForm, passwordEncoder: PasswordEncoder) = TUser(
            accountId = form.accountId ?: 0,
            email = form.email,
            pass = passwordEncoder.encode(form.password),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        fun generateInsertModel(request: UserRegistRequest, passwordEncoder: PasswordEncoder) = TUser(
            accountId = request.accountId,
            email = request.email,
            pass = passwordEncoder.encode(request.password),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        AuthorityUtils.createAuthorityList("USER")

    override fun getPassword(): String = pass

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
