# 30 Practical Docker Volume Questions

This file lists 30 practical DevOps questions to guide reasoning about Docker volumes, persistent storage, troubleshooting, and operational practices.

1. A PostgreSQL container was deleted accidentally. How do you determine whether the database data still exists and recover it?

2. Two containers need to read and write the same files. How would you design storage so both containers can access the data safely?

3. A container writes files to `/app/data`, but after recreating the container all files disappear. How would you troubleshoot this?

4. You have a Docker volume named `prod-db`. Before deleting it, how do you verify which containers use it and what data it contains?

5. A container runs as UID `1001`, but the mounted volume is owned by `root` and the app gets `Permission denied`. How do you diagnose and fix this without running the container as root?

6. The application is writing millions of temporary files to a Docker volume and performance is poor. What would you investigate before moving the data elsewhere?

7. A developer uses a bind mount on Windows for a database container and experiences extreme slowness. What might cause this and what storage design do you recommend instead?

8. You have a Docker volume containing production data. How do you back up the volume without stopping the application, and what risks should you consider?

9. You restore a backup into a new volume, but the application cannot read the files. What checks would you perform?

10. A container is recreated using the same volume, but the app behaves as if data is missing. How do you determine whether the issue is the volume, mount path, or application?

11. Three containers share the same volume; one deletes a file while another reads it. How would you reason about this and prevent data corruption?

12. A container mounts a volume at `/app`, but the image already contained files under `/app` which now appear gone. What happened and how do you investigate?

13. You mount a volume at `/var/lib/mysql` but MySQL starts with an empty database. What should you check first?

14. A Docker volume is consuming 200 GB. How do you identify what's using the space and decide whether cleaning the volume is safe?

15. A container has been deleted but its volume remains. How do you determine whether the volume is still required by another application?

16. You accidentally created many anonymous volumes and the host is low on disk. How do you find unused volumes and clean them safely?

17. Your app needs persistent uploads and may run across multiple hosts. Choose between local volume, bind mount, network filesystem, or object storage — how do you decide?

18. Migrating from a single host to multiple hosts: the app currently uses a local Docker volume. What problems will arise and how do you redesign storage?

19. A container writes a 10 GB file to a volume, is removed, and recreated on another host; the new container cannot see the file. Why, and what architecture avoids this?

20. You need to migrate a Docker volume from Server A to Server B with minimal downtime. How do you approach the migration and verify data integrity?

21. A volume works on Linux but behaves differently on Docker Desktop for Windows. What host-specific issues should you investigate?

22. You ran `docker volume create abc` on Docker Desktop and want to know where the data actually lives. How do you inspect the volume safely without modifying Docker internals?

23. A container needs read-only access to configuration files. How would you mount the storage so the container cannot modify those files?

24. A production container is compromised and the attacker can write to a mounted volume. What damage could occur and how would you reduce impact?

25. Logs in a Docker volume grow until they exhaust disk space. How would you design log rotation and retention to prevent outages?

26. You need to run a database container in production. What storage characteristics would you evaluate when choosing the underlying volume system?

27. Your app performs many random reads/writes and someone suggests S3 as the volume because it's durable. What technical problems do you evaluate before accepting that design?

28. A Compose stack has PostgreSQL, Redis, and an app. PostgreSQL data must persist, Redis can be ephemeral. How do you design volumes for each service?

29. A mounted volume creates files owned by an unexpected UID/GID only in production. How would you investigate environment differences?

30. You design a production app that stores DB data, uploaded images, temp files, and backups. How do you decide which storage (volumes, bind mounts, network storage, databases, object storage) to use for each data class?