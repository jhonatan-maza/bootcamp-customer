package com.nttdata.bootcamp.entity.serverResponse;

import com.nttdata.bootcamp.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServerResponse {
    private Status status;
    private Error error;
    private Object result;
}
