    package com.clockin.model;

    import jakarta.persistence.*;

    import java.time.LocalDate;
    import java.time.OffsetDateTime;

    @Entity
    public class Workday {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "employee_id")
        private Employee employee;
        private LocalDate workdayDate;
        private String dayOfTheWeek;
        private OffsetDateTime morningCheckIn;
        private OffsetDateTime morningCheckOut;
        private OffsetDateTime afternoonCheckIn;
        private OffsetDateTime afternoonCheckOut;

        public Workday(Employee employee, LocalDate workdayDate, String dayOfTheWeek, OffsetDateTime morningCheckIn, OffsetDateTime morningCheckOut, OffsetDateTime afternoonCheckIn, OffsetDateTime afternoonCheckOut) {
            this.employee = employee;
            this.workdayDate = workdayDate;
            this.dayOfTheWeek = dayOfTheWeek;
            this.morningCheckIn = morningCheckIn;
            this.morningCheckOut = morningCheckOut;
            this.afternoonCheckIn = afternoonCheckIn;
            this.afternoonCheckOut = afternoonCheckOut;
        }

        public Workday(){}

        public Long getId() {
            return id;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }

        public LocalDate getWorkdayDate() {
            return workdayDate;
        }

        public void setWorkdayDate(LocalDate workdayDate) {
            this.workdayDate = workdayDate;
        }

        public String getDayOfTheWeek() {
            return dayOfTheWeek;
        }

        public void setDayOfTheWeek(String dayOfTheWeek) {
            this.dayOfTheWeek = dayOfTheWeek;
        }

        public OffsetDateTime getMorningCheckIn() {
            return morningCheckIn;
        }

        public void setMorningCheckIn(OffsetDateTime morningCheckIn) {
            this.morningCheckIn = morningCheckIn;
        }

        public OffsetDateTime getMorningCheckOut() {
            return morningCheckOut;
        }

        public void setMorningCheckOut(OffsetDateTime morningCheckOut) {
            this.morningCheckOut = morningCheckOut;
        }

        public OffsetDateTime getAfternoonCheckIn() {
            return afternoonCheckIn;
        }

        public void setAfternoonCheckIn(OffsetDateTime afternoonCheckIn) {
            this.afternoonCheckIn = afternoonCheckIn;
        }

        public OffsetDateTime getAfternoonCheckOut() {
            return afternoonCheckOut;
        }

        public void setAfternoonCheckOut(OffsetDateTime afternoonCheckOut) {
            this.afternoonCheckOut = afternoonCheckOut;
        }

        @Override
        public String toString() {
            return "WorkdaySummary{" +
                    "id=" + id +
                    ", employee=" + employee +
                    ", workdayDate=" + workdayDate +
                    ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                    ", morningCheckIn=" + morningCheckIn +
                    ", morningCheckOut=" + morningCheckOut +
                    ", afternoonCheckIn=" + afternoonCheckIn +
                    ", afternoonCheckOut=" + afternoonCheckOut +
                    '}';
        }
    }
