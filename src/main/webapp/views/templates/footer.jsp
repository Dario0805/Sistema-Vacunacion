</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>

<script>
    $(document).ready(function() {
        var langUrl = '//cdn.datatables.net/plug-ins/1.13.4/i18n/';
        var locale = '${sessionScope.locale.language}';
        if (locale === 'en') {
            langUrl += 'en-GB.json';
        } else if (locale === 'fr') {
            langUrl += 'fr-FR.json';
        } else if (locale === 'it') {
            langUrl += 'it-IT.json';
        } else {
            langUrl += 'es-ES.json';
        }

        $('.datatable').DataTable({
            language: { url: langUrl },
            responsive: true
        });

        setTimeout(function() {
            $('.alert').alert('close');
        }, 5000);
    });
</script>
</body>
</html>
