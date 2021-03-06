package coffee.machine.model.entity.user;

import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Identified;

/**
 * This class represents User entity.
 *
 * NOTE: in case of admin instance account field will be set with zero-account.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class User implements Identified {
    private int id;
    /**
     * Email field is used as login
     */
    private String email;
    private String password;
    private String fullName;
    private Account account;
    private UserRole role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public Account getAccount() {
        return account;
    }

    public UserRole getRole() {
        return role;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (role != user.role) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return fullName != null ? fullName.equals(user.fullName) : user.fullName == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", account=" + account +
                ", role=" + role +
                '}';
    }

    public static class Builder {
        private User user = new User();

        public Builder setId(int id) {
            user.id = id;
            return this;
        }

        public Builder setEmail(String email) {
            user.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            user.password = password;
            return this;
        }

        public Builder setFullName(String fullName) {
            user.fullName = fullName;
            return this;
        }

        public Builder setRole(UserRole role) {
            user.role = role;
            return this;
        }

        public Builder setAccount(Account account) {
            user.account = account;
            return this;
        }

        public User build() {
            if (user.account == null) {
                user.account = new Account.Builder().build();
            }
            return user;
        }
    }
}
