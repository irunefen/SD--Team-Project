package es.deusto.sd.strava.entity;
import java.time.LocalDate;

public class User {
    private String userId;
    private String email;
    private String name;
    private LocalDate birthdate;
    private Float weight; // kg
    private Float height; // cm
    private Integer maxHeartRate;
    private Integer restHeartRate;
    private String authProviderName;

    // Constructors, getters y setters

    public User() {}

    public User(String userId, String email, String name, LocalDate birthdate, Float weight, Float height,
                Integer maxHeartRate, Integer restHeartRate, String authProviderName) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.weight = weight;
        this.height = height;
        this.maxHeartRate = maxHeartRate;
        this.restHeartRate = restHeartRate;
        this.authProviderName = authProviderName;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getAuthProviderName() {
		return authProviderName;
	}

	public void setAuthProviderName(String authProviderName) {
		this.authProviderName = authProviderName;
	}

   
}

