package mex;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Exchange {
    String base_code;
    String target_code;
    Float conversion_rate;
    Float conversion_result;
    String time_last_update_utc;

    public String getDate(){
        String str = this.time_last_update_utc;
        return str.substring(str.indexOf(",") + 1, str.indexOf(":") - 3 );
    }
}
