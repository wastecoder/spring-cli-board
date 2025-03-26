--liquidbase formatted sql
--2025/03/26 - 15:54
--comment: set unblock_reason nullable

ALTER TABLE BLOCKS ALTER COLUMN unblock_reason DROP NOT NULL;

--rollback ALTER TABLE BLOCKS ALTER COLUMN unblock_reason SET NOT NULL;
