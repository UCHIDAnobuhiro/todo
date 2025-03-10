document.getElementById("sort-order").addEventListener("change", function() {
	const order = this.value;
	sortDeadline(order);
});


const sortDeadline = (order) => {
	let taskList = document.getElementById("task-list");
	let tasks = Array.from(taskList.getElementsByClassName("task"));

	tasks.sort((a, b) => {
		let dateA = new Date(a.getAttribute("data-deadline"));
		let dateB = new Date(b.getAttribute("data-deadline"));

		if (order === "asc") {
			return dateA - dateB;
		} else if (order === "desc") {
			return dateB - dateA;
		} 
	});

	// 並び替えた順番で再配置
	taskList.innerHTML = "";
	tasks.forEach(task => taskList.appendChild(task));
}