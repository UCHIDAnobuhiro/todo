//htmlファイルのロード完成後動作を始まる
document.addEventListener("DOMContentLoaded", () => {
	
	//復元されたいタスクの復元ボタンをIDで取得
	const restoreButton = document.getElementById("confirm-restore");
	let selectedTaskId = null;

	// 各復元ボタンにクリック時のイベントを追加し、th:attr="data-task-id=${task.id}"で設定したdata-task-idを取得
	document.querySelectorAll(".restore-btn").forEach(button => {
		button.addEventListener("click", (event) => {
			
			//ある復元ボタンを押下時、そのボタンのdata-task-idを取得する
			selectedTaskId = event.target.getAttribute("data-task-id");
			console.log("Selected Task ID:", selectedTaskId);
		});
	});

	//復元されたいタスクの復元ボタンが押下時の動作を追加
	restoreButton.addEventListener("click", async () => {
		if (selectedTaskId) {
			try {
				//「is_deletedをfalseにしたい」請求を作成
				const response = await fetch(`/restore/${selectedTaskId}`, {
					method: "PATCH",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify({ is_deleted: false })
				});
				
				//請求に対するresponseは200~299であるかをチェック
				if (response.ok) {
					window.location.href = "/todo/show/deleted";
					console.log("復元成功", selectedTaskId);
				} else {
					console.error("復元失敗", response.status);
				}
			} catch (error) {
				console.error("復元エラー:", error);
			}
		}
	});
});