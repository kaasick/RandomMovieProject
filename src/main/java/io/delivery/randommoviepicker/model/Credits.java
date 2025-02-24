package io.delivery.randommoviepicker.model;

import java.util.List;

//To get the director for the movie info as well

public class Credits {
    private List<CrewMember> crew;

    public List<CrewMember> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewMember> crew) {
        this.crew = crew;
    }

    public static class CrewMember {
        private String job;
        private String name;

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
