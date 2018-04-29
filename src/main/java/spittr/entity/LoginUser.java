package spittr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author ynding 登录用户
 */
@Entity
public class LoginUser {

	@Id
	private Long id;
	@NotNull
	@Size(min = 5, max = 16, message = "{username.size}")
	private String username;
	@NotNull
	@Size(min = 5, max = 25, message = "{password.size}")
	private String password;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
