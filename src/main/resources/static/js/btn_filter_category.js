document.addEventListener("DOMContentLoaded", () => {
	const filterSelect = document.getElementById("category-filter");
	const tasks = document.querySelectorAll(".task");
	
	filterSelect.addEventListener("change", () =>{
		const selectedCategory = filterSelect.value;
		
		tasks.forEach(task => {
			const taskCategory = task.getAttribute("data-category");
			
			if (selectedCategory == "all" || taskCategory == selectedCategory) {
				task.style.display = "block";
			} else {
				task.style.display = "none";
			}
		});
	});
});