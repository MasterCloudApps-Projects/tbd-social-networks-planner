CREATE TABLE public.resource
(
    id             int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    url            varchar(255) NULL,
    creation_date  timestamp NULL,
    deleted_date   timestamp NULL,
    CONSTRAINT resource_pkey PRIMARY KEY (id)
);

ALTER TABLE public.tweet
ALTER COLUMN resource_url TYPE int8 USING (resource_url::bigint);

ALTER TABLE public.tweet
RENAME COLUMN resource_url TO resource_id;

ALTER TABLE public.tweet
ADD CONSTRAINT fk_tweet_resource FOREIGN KEY (resource_id) REFERENCES public.resource (id);