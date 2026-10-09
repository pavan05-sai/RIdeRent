package com.vehiclerental.entity;

import com.vehiclerental.entity.enums.RoleName;
import jakarta.persistence.*;
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true, nullable = false)
    private RoleName name;

    public Role() {
    }

    public Role(Long id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return this.name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private RoleName name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(RoleName name) {
            this.name = name;
            return this;
        }

        public Role build() {
            return new Role(this.id, this.name);
        }
    }
}
