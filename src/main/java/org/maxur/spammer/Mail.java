package org.maxur.spammer;

public class Mail {

    private final Long id;
    private final String subject;
    private final String body;
    private final String responseAddress;

    private Mail(Long id, String subject, String body, String responseAddress) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.responseAddress = responseAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getToAddress() {
        return responseAddress;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }


    static class Builder {
        private String subject;
        private String body;
        private String responseAddress;
        private Long id;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder toAddress(String responseAddress) {
            this.responseAddress = responseAddress;
            return this;
        }

        public Mail build() {
            return new Mail(id, subject, body, responseAddress);
        }
    }
}
