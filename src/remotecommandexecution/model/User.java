package remotecommandexecution.model;

public class User {
    private int id;
    private String username;
    private String fullName;
    private boolean isAdmin;

    public User() {
    }

    public User(int id, String username, String fullName, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

    
}
