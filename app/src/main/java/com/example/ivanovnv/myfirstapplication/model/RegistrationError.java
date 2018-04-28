package com.example.ivanovnv.myfirstapplication.model;

import java.util.List;

public class RegistrationError {

    private ErrorsBean errors;

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public static class ErrorsBean {
        private List<String> email;
        private List<String> name;
        private List<String> password;

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<String> getPassword() {
            return password;
        }

        public void setPassword(List<String> password) {
            this.password = password;
        }

    }

    @Override
    public String toString() {
        String string = "Ошибка\n";
        if (errors.email != null) {string += errors.email.toString() + "\n";}
        if (errors.name != null) {string += errors.name.toString() + "\n ";}
        if (errors.password != null) {string += errors.password.toString()+ "\n";}

        return string;
    }
}
