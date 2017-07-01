alter table budget add fulltext index budeget_search (category_name, description);
alter table debit add fulltext index debit_search (category_name, description);
alter table autodebit add fulltext index autodebit_search (category_name, description);
