package com.mobilewedding.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guestbook")
@CrossOrigin(origins = "*")
public class GuestbookController {

    private final GuestbookRepository guestbookRepository;

    public GuestbookController(GuestbookRepository guestbookRepository) {
        this.guestbookRepository = guestbookRepository;
    }

    @GetMapping
    public List<GuestbookDto.Response> getAll() {
        return guestbookRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public GuestbookDto.Response create(@Valid @RequestBody GuestbookDto.CreateRequest request) {
        GuestbookEntry entry = new GuestbookEntry();
        entry.setName(request.name().trim());
        entry.setMessage(request.message().trim());
        entry.setAttending(request.attending());

        GuestbookEntry saved = guestbookRepository.save(entry);
        return toResponse(saved);
    }

    private GuestbookDto.Response toResponse(GuestbookEntry entry) {
        return new GuestbookDto.Response(
                entry.getId(),
                entry.getName(),
                entry.getMessage(),
                entry.isAttending(),
                entry.getCreatedAt()
        );
    }
}
