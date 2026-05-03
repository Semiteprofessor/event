@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event created successfully",
                        eventService.createEvent(request, user)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getEvent(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event fetched successfully",
                        eventService.getEvent(id)
                )
        );
    }
}