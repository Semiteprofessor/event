@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final WishlistService wishlistService;
    private final EmailService emailService;

    @Transactional
    public Event createEvent(
            CreateEventRequest request,
            User user
    ) {

        String slug = generateSlug(request.getName());

        Event event = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(slug)
                .organizerEmail(user.getEmail())
                .build();

        Event savedEvent = eventRepository.save(event);

        if (request.getWishlist() != null) {

            request.getWishlist()
                    .forEach(item ->
                            wishlistService.createWishlistItem(
                                    savedEvent,
                                    item
                            ));
        }

        emailService.sendEventCreatedEmail(
                user.getEmail(),
                savedEvent.getName()
        );

        return savedEvent;
    }

    public Event getEvent(String id) {

        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new AuthException(404, "Event not found"));
    }

    private String generateSlug(String name) {

        return name.toLowerCase()
                .replace(" ", "-")
                + "-" + System.currentTimeMillis();
    }
}