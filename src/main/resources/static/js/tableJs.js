$(document).ready(function () {
    $('#example,#userTable').DataTable({
        "lengthMenu": [5, 10, 25, 50, 75, 100],
        "responsive": true,  // Enable responsive features
        // Add other DataTable options and configurations here
    });
});
