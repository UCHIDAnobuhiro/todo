document.addEventListener("DOMContentLoaded", () => {
	const modal = document.getElementById("popup-modal");
	const closeButton = document.getElementById("modal-close");
	const cancelButton = document.getElementById("cancel");
	const confirmDeleteButton = document.getElementById("confirm-delete");

	let selectedTaskId = null;

	document.querySelectorAll(".modal-open").forEach(button => {
		button.addEventListener("click", (event) => {
			selectedTaskId = event.target.getAttribute("data-task-id");
			modal.classList.remove("hidden");
			modal.classList.add("flex");
			console.log(selectedTaskId);
		
		})
	})

	closeButton.addEventListener("click", () => {
		modal.classList.remove("flex");
		modal.classList.add("hidden");
	})

	cancelButton.addEventListener("click", () => {
		console.log("cancel");
		modal.classList.add("hidden");
		modal.classList.remove("flex");
	});

	confirmDeleteButton.addEventListener("click", async () => {
		if (selectedTaskId) {
			try {
				const response = await fetch(`/delete/${selectedTaskId}`, {
					method: "PATCH",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ is_deleted: true })
				});
				if (response.ok) {
					modal.classList.add("hidden");
					modal.classList.remove("flex");
					window.location.href = "/todo/show";
					console.log("削除成功", selectedTaskId);
				} else {
					console.error("削除失敗", response.status);
				}
			} catch (error) {
				console.error("削除エラー:", error);
			}
		}

	});

	modal.addEventListener("click", (event) => {
		if (event.target === modal) {
			modal.classList.add("hidden");
			modal.classList.remove("flex");
		}
	});
});