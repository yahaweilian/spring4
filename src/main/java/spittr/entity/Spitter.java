package spittr.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Spitter {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    @NotNull
    @Size(min=5,max=16,message="{username.size}")//{}内不能有空格，否则无效
	private String username;
	
    @NotNull
    @Size(min=5,max=25,message="{password.size}")
	private String password;
	
    @NotNull
    @Size(min=2,max=30,message="{firstName.size}")
	private String firstName;
	
    @NotNull
    @Size(min=2,max=30,message="{lastName.size}")
	private String lastName;
	
    private String email;
    
    public Spitter(){}
    
	public Spitter(Long id, String username, String password, String firstName, String lastName) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Spitter(String username, String password,String firstName, String lastName) {
		this(1L, username, password, firstName, lastName);
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id");
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, "id");
	}
	
}
