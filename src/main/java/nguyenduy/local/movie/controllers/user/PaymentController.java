package nguyenduy.local.movie.controllers.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import nguyenduy.local.movie.config.payment.VNPAYConfig;
import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.models.response.ApiResponse;
import nguyenduy.local.movie.models.response.MessageReponse;
import nguyenduy.local.movie.repositories.UserRepository;
import nguyenduy.local.movie.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserRepository userRepository;

  @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
  @PostMapping("/pay")
  public String getPay(@RequestBody Map<String, Integer> requestBody, HttpServletRequest request) throws UnsupportedEncodingException {
    int amount = requestBody.get("amount");
    long totalPrice = amount * 100;

    String bankCode = "NCB";
    String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
    String vnp_IpAddr = "127.0.0.1";

    String vnp_Version = "2.1.0";
    String vnp_Command = "pay";
    String orderType = "other";
    String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;

    Map<String, String> vnp_Params = new HashMap<>();
    vnp_Params.put("vnp_Version", vnp_Version);
    vnp_Params.put("vnp_Command", vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(totalPrice));
    vnp_Params.put("vnp_CurrCode", "VND");
    vnp_Params.put("vnp_BankCode", bankCode);
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    vnp_Params.put("vnp_OrderInfo", "Nạp " + amount + " coin");
    vnp_Params.put("vnp_OrderType", orderType);
    vnp_Params.put("vnp_Locale", "vn");
    vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_ReturnUrl);
    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
    Collections.sort(fieldNames);

    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();

    Iterator<String> itr = fieldNames.iterator();
    while (itr.hasNext()) {
      String fieldName = itr.next();
      String fieldValue = vnp_Params.get(fieldName);
      if (fieldValue != null && !fieldValue.isEmpty()) {
        hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
            .append('=')
            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

        if (itr.hasNext()) {
          query.append('&');
          hashData.append('&');
        }
      }
    }

    String queryUrl = query.toString();
    String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

    String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;

    return paymentUrl;
  }

  @GetMapping("/payment-result")
  public ResponseEntity<?> paymentResult(@RequestParam Map<String, String> params, @RequestHeader("Authorization") String bearerToken){
    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageReponse("Thiếu hoặc sai định dạng Authorization header"));
    }

    String accessToken = bearerToken.substring(7);

    Long userId = Long.valueOf(jwtService.getUserIdFromJwt(accessToken));
    String vnp_ResponseCode = params.get("vnp_ResponseCode");
    String vnp_Amount = params.get("vnp_Amount");
    String vnp_TxnRef = params.get("vnp_TxnRef");

    if ("00".equals(vnp_ResponseCode)) {
      User user = userRepository.findById(userId).get();
      if (user == null) {
        return ResponseEntity.status(404).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Không tìm thấy user"));
      }
      user.setCoin(user.getCoin() + Double.parseDouble(vnp_Amount));
      userRepository.save(user);
      return ResponseEntity.ok("Thanh toán thành công!");
    } else {
      return ResponseEntity.status(400).body("Thanh toán thất bại!");
    }
  }

  private void appendToHashDataAndQuery(StringBuilder hashData, StringBuilder query, Iterator<String> itr, String fieldName, String fieldValue) throws UnsupportedEncodingException {
    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
        .append('=')
        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

    if (itr.hasNext()) {
      query.append('&');
      hashData.append('&');
    }
  }
}