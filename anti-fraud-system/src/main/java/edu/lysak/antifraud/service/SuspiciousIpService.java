package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.ip.DeletedIpResponse;
import edu.lysak.antifraud.domain.ip.SuspiciousIp;
import edu.lysak.antifraud.domain.ip.SuspiciousIpRequest;
import edu.lysak.antifraud.repository.SuspiciousIpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousIpService {
    private static final String IP_V4_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    private final SuspiciousIpRepository suspiciousIpRepository;

    public SuspiciousIpService(SuspiciousIpRepository suspiciousIpRepository) {
        this.suspiciousIpRepository = suspiciousIpRepository;
    }

    public Optional<SuspiciousIp> addSuspiciousIp(SuspiciousIpRequest request) {
        if (!isValidIpFormat(request.getIp())) {
            throw new IllegalArgumentException();
        }
        Optional<SuspiciousIp> ipFromDb = suspiciousIpRepository.findByIp(request.getIp());
        if (ipFromDb.isPresent()) {
            return Optional.empty();
        }
        SuspiciousIp suspiciousIp = new SuspiciousIp();
        suspiciousIp.setIp(request.getIp());

        SuspiciousIp savedSuspiciousIp = suspiciousIpRepository.save(suspiciousIp);
        return Optional.of(savedSuspiciousIp);
    }

    public boolean isValidIpFormat(String ip) {
        return ip.matches(IP_V4_REGEX);
    }

    public List<SuspiciousIp> getSuspiciousIpList() {
        List<SuspiciousIp> suspiciousIpList = new ArrayList<>();
        suspiciousIpRepository.findAll().forEach(suspiciousIpList::add);
        suspiciousIpList.sort(Comparator.naturalOrder());
        return suspiciousIpList;
    }

    @Transactional
    public Optional<DeletedIpResponse> deleteSuspiciousIp(String ip) {
        if (!isValidIpFormat(ip)) {
            throw new IllegalArgumentException();
        }

        Optional<SuspiciousIp> ipFromDb = suspiciousIpRepository.findByIp(ip);
        if (ipFromDb.isEmpty()) {
            return Optional.empty();
        }
        suspiciousIpRepository.delete(ipFromDb.get());
        DeletedIpResponse response = new DeletedIpResponse(String.format("IP %s successfully removed!", ip));
        return Optional.of(response);
    }

    public boolean existsByIp(String ip) {
        return suspiciousIpRepository.existsByIp(ip);
    }
}
