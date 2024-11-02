package com.teamsantos.easybarber.DTO.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentsHashDTO {
    public long id;
    public boolean future;

    @Override
    public String toString() {
        return "id=" + id + ", future=" + future;
    }
}
