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
		})
	})


	closeButton.addEventListener("click", () => {
		modal.classList.remove("flex");
		modal.classList.add("hidden");
	})

	cancelButton.addEventListener("click", () => {
		modal.classList.add("hidden");
		modal.classList.remove("flex");
	});

	confirmDeleteButton.addEventListener("click", function() {
		console.log(selectedTaskId);
	});

	modal.addEventListener("click", (event) => {
		if (event.target === modal) {
			modal.classList.add("hidden");
			modal.classList.remove("flex");
		}
	});
});