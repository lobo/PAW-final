package ar.edu.itba.paw.persistence;

import java.util.Map;

interface ReverseRowMapper<T> {
    Map<String, Object> toArgs(T t);
}
