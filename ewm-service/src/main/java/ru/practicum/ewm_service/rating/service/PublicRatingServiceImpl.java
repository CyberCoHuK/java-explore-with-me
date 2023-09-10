package ru.practicum.ewm_service.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.dto.EventDtoRate;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.rating.model.Rate;
import ru.practicum.ewm_service.rating.repository.RateRepository;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;
import ru.practicum.ewm_service.user.dto.UserDtoRate;
import ru.practicum.ewm_service.user.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicRatingServiceImpl implements PublicRatingService {
    private final RateRepository rateRepository;
    private final RequestRepository requestRepository;
    private final Client statClient;


    @Override
    public Collection<EventDtoRate> getEventsRating(Boolean sort, int size, int from) {
        PageRequest page = PageRequest.of(from / size, size);
        Page<Rate> rateList = rateRepository.findAll(page);
        List<EventDtoRate> events = rateList.stream()
                .map(Rate::getEvent)
                .map(EventMapper::eventDtoRate)
                .distinct()
                .collect(Collectors.toList());
        events.forEach(e ->
                e.setConfirmedRequests(requestRepository.findConfirmedRequest(e.getId())));
        events.forEach(e -> e.setViews(statClient.getView(e.getId())));
        for (EventDtoRate event : events) {
            Long like = rateList.stream()
                    .filter(rate -> rate.getRate().equals(TRUE))
                    .filter(rate -> rate.getEvent().getId().equals(event.getId()))
                    .count();
            Long dislike = rateList.stream()
                    .filter(rate -> rate.getRate().equals(FALSE))
                    .filter(rate -> rate.getEvent().getId().equals(event.getId()))
                    .count();
            event.setLike(like);
            event.setDislike(dislike);
        }
        return events;
    }

    @Override
    public Collection<UserDtoRate> getUsersRating(Boolean sort, int size, int from) {
        PageRequest page = PageRequest.of(from / size, size);
        Page<Rate> rateList = rateRepository.findAll(page);
        List<UserDtoRate> users = rateList.stream()
                .map(rate -> rate.getEvent().getInitiator())
                .map(UserMapper::toUserDtoRate)
                .distinct()
                .collect(Collectors.toList());
        for (UserDtoRate user : users) {
            Long like = rateList.stream()
                    .filter(rate -> rate.getRate().equals(TRUE))
                    .filter(rate -> rate.getEvent().getInitiator().getId() == (user.getId()))
                    .count();
            Long dislike = rateList.stream()
                    .filter(rate -> rate.getRate().equals(FALSE))
                    .filter(rate -> rate.getEvent().getInitiator().getId() == (user.getId()))
                    .count();
            user.setLike(like);
            user.setDislike(dislike);
        }
        return users;
    }
}
