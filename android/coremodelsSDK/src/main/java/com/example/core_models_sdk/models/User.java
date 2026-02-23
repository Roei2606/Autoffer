package com.example.core_models_sdk.models;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "profileType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FactoryUser.class, name = "FACTORY"),
        @JsonSubTypes.Type(value = User.class, name = "ARCHITECT"),
        @JsonSubTypes.Type(value = User.class, name = "PRIVATE_CUSTOMER")
})
public class User {

    @JsonProperty("id")
    private String id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("profileType")
    private UserType profileType;

    @JsonProperty("registeredAt")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime registeredAt;

    @JsonProperty("chats")
    private List<String> chats;

    @JsonProperty("photoBytes") // ✅ שדה אחיד לתמונת פרופיל או לוגו מפעל
    private byte[] photoBytes;

    public User() {
        // Default constructor for Jackson
    }

    public User(String id, String firstName, String lastName, String email, String password,
                String phoneNumber, String address, UserType profileType,
                LocalDateTime registeredAt, List<String> chats, byte[] photoBytes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileType = profileType;
        this.registeredAt = registeredAt;
        this.chats = chats;
        this.photoBytes = photoBytes;
    }

    // Getters and Setters

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public UserType getProfileType() { return profileType; }

    public void setProfileType(UserType profileType) { this.profileType = profileType; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }

    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public List<String> getChats() { return chats; }

    public void setChats(List<String> chats) { this.chats = chats; }

    public byte[] getPhotoBytes() { return photoBytes; }

    public void setPhotoBytes(byte[] photoBytes) { this.photoBytes = photoBytes; }

    @Override
    public String toString() {
        return "🧍 User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", profileType='" + profileType + '\'' +
                ", registeredAt=" + registeredAt +
                ", chats=" + chats +
                ", photoBytes=" + (photoBytes != null ? "[...]" : "null") +
                '}';
    }


}
