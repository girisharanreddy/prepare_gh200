# 30 Practical Docker Bind-Mount Questions

This file lists 30 practical DevOps questions to help reason about Docker bind mounts, their effects, and common troubleshooting or security considerations.

1. You have an Nginx container and your HTML files live on the host at `/opt/web/html`. How do you make them available at `/usr/share/nginx/html` inside the container?

2. A developer modifies a file inside `/app` in a container and the corresponding host file updates immediately. How do you determine which mount is responsible?

3. You start a container with a bind mount from `/home/dev/project` to `/app`, but `/app` inside the container is empty. What should you check?

4. You bind-mount `/host/config` to `/app/config`, and files that were in the image at `/app/config` are no longer visible. Why can this happen and how do you verify it?

5. A developer wants to edit source code on their laptop while the app runs in a container. How would you design the bind mount for a smooth developer experience?

6. A container needs to read configuration files from `/etc/myapp` on the host but must never modify them. How would you configure the mount and permissions?

7. You bind-mount `/host/data` to `/app/data`, but the application gets `Permission denied` when writing. What would you investigate?

8. A container creates files in a bind-mounted directory, and those files are owned by `root` on the host. What can cause this and how do you fix ownership?

9. A developer accidentally deletes a host file from inside the container. How would you prevent containers from modifying critical host files?

10. You change a file on the host, but the application inside the container continues reading the old contents. What might be happening?

11. You mount `/opt/application:/app`. The app expects `/app/logs` but logs appear under `/opt/application/logs`. How do you verify both paths refer to the same files?

12. A container works without a bind mount but fails once you add one. What categories of issues should you investigate?

13. Your image contains `/app/config`, `/app/scripts`, and `/app/application.jar`. You bind-mount `/host/config` to `/app`. What happens to the original files under `/app`, and how can that break the app?

14. Multiple containers need the same host directory. How would you configure mounts and what concurrency problems could arise?

15. Two containers write to the same bind-mounted directory. Container A writes successfully, but Container B sometimes sees incomplete files. How would you troubleshoot this?

16. A container needs access to `/var/log/myapp` on the host for append/read only. How do you design the mount and permissions to limit risk?

17. You deploy the same app to 10 Linux servers, each with a different host path for data. How do you make the bind-mount configuration portable and maintainable?

18. A Docker Compose file uses a relative path for a bind mount. The app behaves differently depending on the directory where `docker-compose` is started. How would you diagnose and fix this?

19. A bind-mounted directory contains millions of files and the containerized app becomes extremely slow. What would you investigate and optimize?

20. File-change detection is reliable on native Linux but unreliable or slow on Docker Desktop for Windows. What areas would you investigate?

21. You bind-mount `/etc` into a production container. What risks does this create and how would you redesign the deployment to reduce risk?

22. A CI pipeline bind-mounts the GitHub Actions workspace into `/workspace`. The container modifies files and later CI steps fail. How do you troubleshoot and prevent this?

23. A container runs as UID `1000`, while the host directory is owned by UID `2000`. The app cannot write. How do you resolve this without running the container as root?

24. Your container needs TLS certificates from the host. How would you mount certificates while minimizing the container's ability to modify them?

25. A production container maps `/host/app-data` to `/app/data`. An administrator accidentally deletes `/host/app-data`. What happens inside the running container and how do you protect against this?

26. A container restarts fine on the same host but after moving to another server the bind mount fails because the source directory doesn't exist. How do you design the deployment to handle missing host paths?

27. Your Docker host uses SELinux. A container bind-mounted to `/data` gets permission errors even though Unix permissions look correct. What would you check?

28. You want to expose only a single host file `/etc/myapp/config.yaml` to a container rather than the entire directory. How would you configure the bind mount and what edge cases should you consider?

29. A security review finds a container bind-mounted to `/var/run/docker.sock`. Why is this a serious concern and what should you investigate?

30. You're designing a production deployment where code, configuration, logs, and data are all bind-mounted. How do you decide which items should remain bind-mounted, which should use other storage (volumes, object storage), and which should not be mounted at all?