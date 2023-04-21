package mex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@Controller
public class WebController {
    @RequestMapping(value = "/")
    public String defaultPage() {
        return "index";
    }

    @RequestMapping(value = "/index")
    public String homePage() {
        return "index";
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public String resultPage(@RequestParam(value = "base_code") String base_code,
                             @RequestParam(value = "target_code") String target_code,
                             @RequestParam(value = "amount") int amount,
                             @RequestParam(value = "activity") String activityType,
                             Model model) {

        Activity activity = requestActivity(activityType);
        Quote quote = requestQuote();
        Exchange exchange = requestExchange(base_code, target_code, amount);


        if(amount < 0 || base_code.equals(target_code) || activity == null ||
        quote == null || exchange == null) return "error";

        model.addAttribute("activity", activity);
        model.addAttribute("quote", quote);
        model.addAttribute("amount", amount);
        model.addAttribute("exchange", exchange);

        return "result";
    }

    private Exchange requestExchange(String base_code, String target_code, int amount){
        String auth = "9e79975dc81c13d3d4101a7a";
        String exchangeUri = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%d", auth, base_code, target_code, amount);
        RestTemplate restTemplate = new RestTemplate();
        Exchange exchange = restTemplate.getForObject(exchangeUri, Exchange.class);

        return exchange;
    }

    private Quote requestQuote(){
        RestTemplate restTemplate = new RestTemplate();
        String quoteUri = "https://programming-quotes-api.herokuapp.com/quotes/random";
        Quote quote = restTemplate.getForObject(quoteUri, Quote.class);

        return quote;
    }

    private Activity requestActivity(String activityType){
        RestTemplate restTemplate = new RestTemplate();

        String activityUri = String.format("https://www.boredapi.com/api/activity?type=%s", activityType);
        Activity activity = restTemplate.getForObject(activityUri, Activity.class);

        return activity;
    }
}