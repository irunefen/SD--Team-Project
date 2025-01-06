package es.deusto.sd.strava.entity;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.SequencedSet;
import java.util.Set;
import java.util.SortedSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, unique = true)
    private String email;
	
	@Column(nullable = false)
    private String name;
	
	@Column(columnDefinition = "DATE", name="birthdate")
    private LocalDate birthdate;
	
	@Column
    private Float weight; // kg
	
	@Column
    private Float height; // cm
	
	@Column(name = "max_heart_rate")
    private Integer maxHeartRate;
	
	@Column(name = "rest_heart_rate")
    private Integer restHeartRate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_accepted_challenges", 
			joinColumns = @JoinColumn(name = "users_id"), 
			inverseJoinColumns = @JoinColumn(name = "challenge_id"),
			uniqueConstraints = @UniqueConstraint(columnNames = {"users_id", "challenge_id"})
	)
	private Set<Challenge> acceptedChallenges;

    public User() {}

    public User(String email, String name, LocalDate birthdate, Float weight, 
    			Float height, Integer maxHeartRate, Integer restHeartRate) {
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.weight = weight;
        this.height = height;
        this.maxHeartRate = maxHeartRate;
        this.restHeartRate = restHeartRate;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(email, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id);
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Integer getMaxHeartRate() {
		return maxHeartRate;
	}

	public void setMaxHeartRate(Integer maxHeartRate) {
		this.maxHeartRate = maxHeartRate;
	}

	public Integer getRestHeartRate() {
		return restHeartRate;
	}

	public void setRestHeartRate(Integer restHeartRate) {
		this.restHeartRate = restHeartRate;
	}
	
	public Set<Challenge> getAcceptedChallenges() {
		return acceptedChallenges;
	}
	
   
}

