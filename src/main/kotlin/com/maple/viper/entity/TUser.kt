package com.maple.viper.entity

import com.maple.viper.form.UserRegistForm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
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
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "password", nullable = false)
//    @Convert(converter = CryptoConverter::class)
    val pass: String,
) : UserDetails {
    companion object {
        fun generateInsertModel(form: UserRegistForm) = TUser(
            email = form.email,
            pass = form.password
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
