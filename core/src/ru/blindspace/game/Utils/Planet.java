package ru.blindspace.game.Utils;

/**
 * Created by ihzork on 24.11.16.
 */
public class Planet {

        public int id;
        public String name;

        public String[] distance;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Planet planet = (Planet) o;

            if (id != planet.id) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return id;
        }
}
