package edu.lysak.account.service;

import edu.lysak.account.domain.Payment;
import edu.lysak.account.domain.User;
import edu.lysak.account.dto.PaymentRequest;
import edu.lysak.account.dto.PaymentResponse;
import edu.lysak.account.exception.InvalidRequestException;
import edu.lysak.account.repository.PaymentRepository;
import edu.lysak.account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentResponse uploadPayments(List<PaymentRequest> payments) {
        Map<String, List<PaymentRequest>> paymentsByUser = payments.stream()
            .collect(Collectors.groupingBy(PaymentRequest::getEmployee));

        for (var entry : paymentsByUser.entrySet()) {
            String userEmail = entry.getKey();
            User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidRequestException("An employee must be among the users of our service"));

            List<PaymentRequest> userPayments = entry.getValue();
            List<YearMonth> periods = userPayments.stream()
                .map(PaymentRequest::getPeriod)
                .collect(Collectors.toList());
            if (containsDuplicatePeriods(periods)) {
                log.warn("The period for which the salary is paid must be unique for each employee");
                throw new InvalidRequestException("The period for which the salary is paid must be unique for each employee");
            }

            for (PaymentRequest userPayment : userPayments) {
                Payment payment = mapToPaymentEntity(user.getUserId(), userPayment);
                paymentRepository.save(payment);
            }
        }

        return PaymentResponse.builder().status("Added successfully!").build();
    }

    @Transactional
    public PaymentResponse updatePayment(PaymentRequest paymentRequest) {
        User user = userRepository.findByEmail(paymentRequest.getEmployee())
            .orElseThrow(() -> new InvalidRequestException("An employee must be among the users of our service"));

        int updateCount = paymentRepository.updatePayment(
            user.getUserId(),
            paymentRequest.getPeriod(),
            paymentRequest.getSalary()
        );
        if (updateCount <= 0) {
            log.warn("No payment record found for provided period");
            throw new InvalidRequestException("No payment record found for provided period");
        }

        return PaymentResponse.builder().status("Updated successfully!").build();
    }

    public Object getUserPayments(User user, String period) {
        User userFromDb = userRepository.findUserWithPaymentsByUserId(user.getUserId())
            .orElseThrow(() -> new UsernameNotFoundException("User with provided email not found."));

        if (period == null) {
            return userFromDb.getPayments().stream()
                .sorted(Comparator.comparing(Payment::getPeriod).reversed())
                .map(it -> mapToPaymentResponse(userFromDb, it))
                .toList();
        }

        YearMonth parsedPeriod = getParsedPeriod(period);
        return userFromDb.getPayments().stream()
            .filter(it -> parsedPeriod.equals(it.getPeriod()))
            .findFirst()
            .map(it -> mapToPaymentResponse(userFromDb, it))
            .orElse(new PaymentResponse());
    }

    private YearMonth getParsedPeriod(String period) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-uuuu");
        try {
            return YearMonth.parse(period, formatter);
        } catch (DateTimeParseException ex) {
            throw new InvalidRequestException("Wrong date!");
        }
    }

    private PaymentResponse mapToPaymentResponse(User userFromDb, Payment it) {
        return PaymentResponse.builder()
            .name(userFromDb.getName())
            .lastname(userFromDb.getLastname())
            .period(mapToPeriodString(it.getPeriod()))
            .salary(mapToSalaryString(it.getSalary()))
            .build();
    }

    private String mapToPeriodString(YearMonth period) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMMM-uuuu");
        return period.format(f);
    }

    private String mapToSalaryString(long salary) {
        return String.format("%s dollar(s) %s cent(s)", salary / 100, salary % 100);
    }

    private Payment mapToPaymentEntity(long userId, PaymentRequest paymentRequest) {
        return Payment.builder()
            .userId(userId)
            .period(paymentRequest.getPeriod())
            .salary(paymentRequest.getSalary())
            .build();
    }

    private boolean containsDuplicatePeriods(List<YearMonth> periods) {
        HashSet<YearMonth> uniquePeriods = new HashSet<>(periods);
        return periods.size() != uniquePeriods.size();
    }
}
