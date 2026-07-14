SELECT (ep.serialized_event::jsonb) -> 'withExternalId' AS external_identifier,
       ep.publication_date,
       ep.completion_attempts
FROM event_publication ep;

SELECT COUNT(id)
FROM event_publication;

SELECT COUNT(id)
FROM event_publication_archive;

SELECT ep.serialized_event::jsonb -> 'customerRequest' -> 'email' -> 'email' AS email
FROM event_publication ep
WHERE ep.serialized_event::jsonb -> 'customerRequest' -> 'email' ->> 'email' = 'bhramar.bandopadhyay@rediffmail.com';

SELECT *
FROM event_publication_archive epa
WHERE epa.serialized_event::jsonb -> 'customerRequest' -> 'email' ->> 'email' = 'sher.menon@gmail.com';

select * from event_publication where event_publication.completion_attempts <= 15;

select completion_attempts from event_publication;