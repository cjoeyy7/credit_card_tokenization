package springboot.credit.card.token.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.credit.card.token.model.CreditCard;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CreditCardController {

    @PostMapping("tokenize")
    public CreditCard tokenize(@RequestParam("cardNumber") String cardNumber, @RequestParam("cvv") String cvv, @RequestParam("expiryYear") String expiryYear) {

        String token = getJWTToken(cardNumber, cvv, expiryYear);
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(cardNumber);
        creditCard.setCvv(cvv);
        creditCard.setExpiryYear(expiryYear);
        creditCard.setToken(token);
        return creditCard;

    }

    private String getJWTToken(String cardNumber, String cvv, String expiryYear) {
        String secretKey = "honeyBadger";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("USER_CARD");

        String token = Jwts
                .builder()
                .setId("myJWT")
                .setSubject(cardNumber)
                .setSubject(cvv)
                .setSubject(expiryYear)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}












